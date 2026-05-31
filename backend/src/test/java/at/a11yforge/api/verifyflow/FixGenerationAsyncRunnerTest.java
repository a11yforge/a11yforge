package at.a11yforge.api.verifyflow;

import static org.mockito.Mockito.*;

import at.a11yforge.api.fixproposal.*;
import at.a11yforge.api.llm.ChatProviderFactory;
import at.a11yforge.api.llm.ProviderType;
import at.a11yforge.api.page.Page;
import at.a11yforge.api.scan.Scan;
import at.a11yforge.api.violation.Violation;
import at.a11yforge.api.violation.ViolationRepository;
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
  }

