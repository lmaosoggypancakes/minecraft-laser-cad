<template>
  <div class="space-y-32">
    <div
      class="w-screen h-screen z-10 flex items-center justify-center"
      v-if="id == ''"
    >
      <form class="flex space-x-4" @submit.prevent="setNewId">
        <input
          type="text"
          placeholder="Enter your CraftKit ID"
          class="input input-bordered w-full max-w-xs"
          v-model="editedId"
        />
        <button
          class="btn btn-primary px-8"
          type="submit"
          :disabled="editedId == ''"
        >
          GO
        </button>
      </form>
    </div>

    <div
      class="w-screen h-screen bg-content z-10 flex items-center justify-center"
      v-else
    >
      <canvas
        width="600"
        height="800"
        ref="canvas"
        class="bg-base-content rounded-md"
      >
      </canvas>
      <form
        @submit.prevent
        class="flex justify-center items-center space-x-8 place-self-end max-w-md w-full mb-4 absolute"
      >
        <button
          class="btn btn-info btn-outline flex flex-col justify-center items-center"
          v-if="currentPage > 1"
          @click="previousPage()"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="32"
            height="32"
            viewBox="0 0 24 24"
          >
            <path
              fill="currentColor"
              d="m7.825 13l5.6 5.6L12 20l-8-8l8-8l1.425 1.4l-5.6 5.6H20v2z"
            />
          </svg>
        </button>
        <!-- <input
          type="range"
          min="0"
          max="20"
          class="range range-primary"
          step="1"
          v-model="faces"
        />
        <button class="btn btn-primary btn-outline" type="submit">
          Make {{ faces }} faces
        </button> -->
        <button
          class="btn btn-primary"
          @click="download()"
          v-if="0 < currentPage && currentPage < pages.length"
        >
          Download dxf
        </button>
        <button
          v-if="currentPage < pages.length - 1"
          @click="nextPage()"
          class="btn btn-info btn-outline"
          type="submit"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="32"
            height="32"
            viewBox="0 0 24 24"
          >
            <path
              fill="currentColor"
              d="M16.175 13H4v-2h12.175l-5.6-5.6L12 4l8 8l-8 8l-1.425-1.4z"
            />
          </svg>
        </button>
      </form>
    </div>
  </div>
</template>
<script setup lang="ts">
import type { Box } from "@/types";
import DxfParser from "dxf-parser";
import { splitArray } from "@/util";
const editedId = ref("");
const canvas = ref<HTMLCanvasElement | null>(null);

const pages = ref<string[]>([]);
const dxfContent = ref("");
const currentPage = ref(0);
const id = ref("");

onMounted(() => {
  const route = useRoute();
  id.value = (route.query.id as string) || "";
});

const box = ref<Box | null>(null);

watch(id, async (val) => {
  if (val) {
    // const res = await useFetch(`/api/boxes/${val}`);
    // box.value = res.data.value as any;
    // if (!box.value) {
    //   console.error("no blocks");
    //   return;
    // }
    // const splicedBlocks = splitArray(box.value.blocks, 10);
    // splicedBlocks.forEach(async (splice, index) => {
    //   // create a page from this
    //   const dxf = await $fetch(`/api/faces/10?page=${index + 1}`);

    //   pages.value.push(dxf);
    // });

    const spliced = splitArray(Array(100), 10);
    for await (const [i, _] of spliced.entries()) {
      const dxf = await $fetch(`/api/faces/10?page=${i + 1}`);
      pages.value.push(dxf);
    }
  }
});
const setNewId = () => {
  id.value = editedId.value;
};

const clearCanvas = () => {
  const ctx = canvas.value?.getContext("2d");
  if (!ctx) {
    console.error("ERROR: ctx not yet rendered");
    return;
  }

  if (!canvas.value) {
    console.error("ERROR: canvas not yet rendered");
    return;
  }
  ctx.clearRect(0, 0, canvas.value.width, canvas.value.height);
};
watch(dxfContent, (val) => {
  if (val == "") return;
  const ctx = canvas.value?.getContext("2d");
  if (!ctx) {
    console.error("ERROR: ctx not yet rendered");
    return;
  }

  if (!canvas.value) {
    console.error("ERROR: canvas not yet rendered");
    return;
  }
  ctx.clearRect(0, 0, canvas.value.width, canvas.value.height);
  const parser = new DxfParser();
  try {
    const parsedDxf = parser.parse(val);
    if (!parsedDxf) {
      return;
    }
    for (const entity of parsedDxf.entities) {
      if (entity.type === "INSERT" && entity.name === "face") {
        // render the inserted block
        for (const subEntity of parsedDxf.blocks.face.entities) {
          const startPoint = subEntity.vertices[0];
          const endPoint = subEntity.vertices[1];
          ctx.beginPath();
          ctx.moveTo(
            entity.position.x + startPoint.x,
            entity.position.y + startPoint.y
          );
          ctx.lineTo(
            entity.position.x + endPoint.x,
            entity.position.y + endPoint.y
          );
          ctx.stroke();
        }
      }
      if (entity.type === "TEXT") {
        ctx.font = "12px serif";
        ctx.fillText(entity.text, entity.startPoint.x, entity.startPoint.y);
      }
      // Add more cases for other entity types as needed
    }
  } catch (err) {
    return console.error(err);
  }
});

const nextPage = () => {
  currentPage.value++;
  clearCanvas();
  dxfContent.value = pages.value[currentPage.value - 1];
};

const previousPage = () => {
  currentPage.value--;
  clearCanvas();
  dxfContent.value = pages.value[currentPage.value - 1];
};

const download = () => {
  const dxfToSave = pages.value[currentPage.value - 1];

  // Create a Blob from the content
  const blob = new Blob([dxfToSave], { type: "text/plain" });

  // Create a link element
  const link = document.createElement("a");

  // Set the download attribute to specify the filename
  link.download = `page_${currentPage.value}.dxf`;

  // Create a URL for the Blob and set it as the href of the link
  link.href = window.URL.createObjectURL(blob);

  // Append the link to the document
  document.body.appendChild(link);

  // Trigger a click on the link to start the download
  link.click();

  // Remove the link from the document
  document.body.removeChild(link);
};
</script>
