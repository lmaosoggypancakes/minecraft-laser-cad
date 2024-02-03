<template>
  <div class="flex justify-center w-full">
    <div class="stats w-full max-w-3xl">
      <div class="stat hover:-translate-y-2 transition">
        <div class="stat-figure text-primary">
          <Icon name="octicon:heart-24" class="h-12 w-12" />
        </div>
        <div class="stat-title">Total Likes</div>
        <div class="stat-value text-primary">25.6K</div>
        <div class="stat-desc">21% more than last month</div>
      </div>

      <div class="stat hover:-translate-y-2 transition">
        <div class="stat-figure text-secondary">
          <Icon name="octicon:zap-24" class="h-12 w-12" />
        </div>
        <div class="stat-title">Page Views</div>
        <div class="stat-value text-secondary">2.6M</div>
        <div class="stat-desc">21% more than last month</div>
      </div>

      <div class="stat hover:-translate-y-2 transition">
        <div class="stat-figure text-secondary">
          <Icon name="octicon:graph-24" class="h-12 w-12" />
        </div>
        <div class="stat-value text-secondary">86%</div>
        <div class="stat-title">Tasks done</div>
        <div class="stat-desc text-secondary">31 tasks remaining</div>
      </div>
    </div>
  </div>
</template>
