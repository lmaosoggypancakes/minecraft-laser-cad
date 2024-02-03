import { createNManyBlocks } from "~/util";

export default defineEventHandler(async (event) => {
  const num = parseInt(event.context.params?.num || "") as number;
  const page = parseInt(getQuery(event).page as string);

  if (!Number.isInteger(num))
    throw createError({
      statusCode: 400,
      statusMessage: "bro you gotta give me an id",
    });

  if (!page) {
    throw createError({
      statusCode: 400,
      statusMessage: "bro you gotta give me a page number",
    });
  }
  return createNManyBlocks(num, page);
});
