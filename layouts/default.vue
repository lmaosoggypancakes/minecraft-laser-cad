<template>
  <div>
    <Navbar />
    <slot />
    <Footer />
  </div>
</template>

<script setup lang="ts">
useHead({
  htmlAttrs: {
    lang: "en",
    "data-theme": "dracula",
  },
});
</script>
