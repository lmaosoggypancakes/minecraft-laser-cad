export default defineEventHandler(async (event) => {
    const id = parseInt(event.context.params?.id || '') as number

    if (!Number.isInteger(id)) throw createError({
        statusCode: 400,
        statusMessage: 'bro you gotta give me an id'
    })

    return await event.context.prisma.box.findUnique({
        where: {
            id
        },
        include: {
            blocks: true
        }
    })
})
