package at.a11yforge.api.verifyflow;

import static org.mockito.Mockito.*;

import at.a11yforge.api.fixproposal.*;
import at.a11yforge.api.llm.ChatProvider;
import at.a11yforge.api.llm.ChatProviderFactory;
import at.a11yforge.api.llm.FixGenerationResponseDTO;
import at.a11yforge.api.llm.ProviderType;
import at.a11yforge.api.page.Page;
import at.a11yforge.api.scan.Scan;
import at.a11yforge.api.violation.Impact;
import at.a11yforge.api.violation.Violation;
import at.a11yforge.api.violation.ViolationRepository;
import at.a11yforge.api.violation.ViolationSource;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FixGenerationAsyncRunnerTest {

  @Mock FixProposalRepository fixProposalRepository;
  @Mock ChatProviderFactory chatProviderFactory;
  @Mock FixProposalService fixProposalService;
  @Mock ViolationRepository violationRepository;
  @InjectMocks FixGenerationAsyncRunner fixGenerationAsyncRunner;

  @Test
  public void skipsWhenProviderIsNone() {
    Scan scan = mock(Scan.class);
    when(scan.getLlmProvider()).thenReturn(ProviderType.NONE);
    Page page = mock(Page.class);
    when(page.getScan()).thenReturn(scan);

    Violation violation = mock(Violation.class);
    when(violation.getPage()).thenReturn(page);

    FixProposal proposal = mock(FixProposal.class);
    when(proposal.getViolation()).thenReturn(violation);

    when(fixProposalRepository.findById(1L)).thenReturn(Optional.of(proposal));

    fixGenerationAsyncRunner.run(1L);

    verify(proposal).setStatus(FixProposalStatus.SKIPPED_NO_PROVIDER);
    verify(fixProposalRepository).save(proposal);
  }

  @Test
  public void setsVerifiedWhenFixPasses() {
    Scan scan = mock(Scan.class);
    when(scan.getLlmProvider()).thenReturn(ProviderType.ANTHROPIC);
    when(scan.getId()).thenReturn(10L);

    Page page = mock(Page.class);
    when(page.getScan()).thenReturn(scan);

    Violation violation = mock(Violation.class);
    when(violation.getPage()).thenReturn(page);

    when(violation.getSource()).thenReturn(ViolationSource.AXE_VIOLATION);
    when(violation.getRuleId()).thenReturn("image-alt");
    when(violation.getImpact()).thenReturn(Impact.CRITICAL);
    when(violation.getHtmlSnippet()).thenReturn("<img>");
    when(violation.getTargetSelector()).thenReturn("img");
    when(violation.getDescription()).thenReturn("desc");
    when(page.getRenderedHtml()).thenReturn("<html>");

    FixProposal fixProposal = mock(FixProposal.class);
    when(fixProposal.getViolation()).thenReturn(violation);

    when(fixProposalRepository.findById(1L)).thenReturn(Optional.of(fixProposal));

    ChatProvider provider = mock(ChatProvider.class);
    when(chatProviderFactory.getProvider(ProviderType.ANTHROPIC)).thenReturn(provider);
    when(provider.generateFix(any()))
        .thenReturn(new FixGenerationResponseDTO("<fixed/>", "claude", "v1", true, null));

    when(violationRepository.findByPage_Scan_Id(10L)).thenReturn(List.of());

    when(fixProposalService.verifyFix(any(), any(), any(), any(), any(), any()))
        .thenReturn(FixProposalStatus.VERIFIED);

    fixGenerationAsyncRunner.run(1L);

    verify(fixProposal).setStatus(FixProposalStatus.VERIFIED);
    verify(fixProposalRepository).save(fixProposal);
  }
}
