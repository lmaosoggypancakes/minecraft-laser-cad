<template>
  <div class="hero min-h-screen bg-base-200">
    <div class="hero-content flex-col lg:flex-row-reverse">
      <div class="indicator w-full max-w-md md:ml-8">
        <img src="@/assets/images/nunu.jpeg" class="rounded-md" />
      </div>
      <div class="max-w-2xl text-center md:text-left">
        <h1 class="text-5xl font-bold">Amplify your Minecraft Creations!</h1>
        <p class="py-6">
          Let your minecraft ideas come to life with an in-game CAD software
          that can export straight to a laser cut building!
        </p>
        <NuxtLink to="/creator" class="btn btn-primary">Get Started</NuxtLink>
      </div>
    </div>
  </div>
</template>
