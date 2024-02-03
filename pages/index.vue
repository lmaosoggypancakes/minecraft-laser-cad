<template>
  <div class="space-y-32">
    <IndexHero />
    <IndexFourSteps />
    <IndexAbout />
    <IndexStats />
    <IndexFaq />
  </div>
</template>
<script setup lang="ts">
definePageMeta({ middleware: "auth" });
</script>
