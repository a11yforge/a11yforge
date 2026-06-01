<script setup lang="ts">
import Carousel from "primevue/carousel";
import { wcagRules } from "@/data/wcagRules"
</script>


<template>
  <Carousel
    :value="wcagRules"
    :numVisible="1"
    :numScroll="1">
    circular
    :autoplayInterval="4000"
    aria-label="WCAG-Barrierefreiheits-Regeln"
  >
    <template #item="{ data }">
      <div class="p-4">
        <h3 class="font-semibold">{{ data.title }}</h3>
        <span class="text-sm text-gray-400">WCAG {{ data.wcag }}</span>
        <p class="mt-2">{{ data.description }}</p>
      </div>
    </template>
  </Carousel>
</template>
