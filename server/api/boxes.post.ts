import type { Prisma } from "@prisma/client";

export default defineEventHandler(async (event) => {
  const {
    blocks,
    name,
  }: { blocks: Prisma.BlockCreateManyInput; name: string } = await readBody(
    event
  );
  const result = await event.context.prisma.box.create({
    data: {
      blocks: {
        createMany: {
          data: blocks,
        },
      },
      name,
    },
  });

  return { id: result.id };
});
