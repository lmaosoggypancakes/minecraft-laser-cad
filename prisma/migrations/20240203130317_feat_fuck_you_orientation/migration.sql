-- CreateEnum
CREATE TYPE "BlockType" AS ENUM ('BLOCK', 'STAIRS', 'SLAB');

-- CreateTable
CREATE TABLE "Block" (
    "id" SERIAL NOT NULL,
    "x" INTEGER NOT NULL,
    "y" INTEGER NOT NULL,
    "z" INTEGER NOT NULL,
    "name" TEXT NOT NULL,
    "type" "BlockType" NOT NULL,
    "boxId" INTEGER,

    CONSTRAINT "Block_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "BlockNode" (
    "id" TEXT NOT NULL,
    "blockId" INTEGER NOT NULL,

    CONSTRAINT "BlockNode_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "Box" (
    "id" SERIAL NOT NULL,
    "name" TEXT NOT NULL,

    CONSTRAINT "Box_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "_connectedNodes" (
    "A" TEXT NOT NULL,
    "B" TEXT NOT NULL
);

-- CreateIndex
CREATE UNIQUE INDEX "_connectedNodes_AB_unique" ON "_connectedNodes"("A", "B");

-- CreateIndex
CREATE INDEX "_connectedNodes_B_index" ON "_connectedNodes"("B");

-- AddForeignKey
ALTER TABLE "Block" ADD CONSTRAINT "Block_boxId_fkey" FOREIGN KEY ("boxId") REFERENCES "Box"("id") ON DELETE SET NULL ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "BlockNode" ADD CONSTRAINT "BlockNode_blockId_fkey" FOREIGN KEY ("blockId") REFERENCES "Block"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "_connectedNodes" ADD CONSTRAINT "_connectedNodes_A_fkey" FOREIGN KEY ("A") REFERENCES "BlockNode"("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "_connectedNodes" ADD CONSTRAINT "_connectedNodes_B_fkey" FOREIGN KEY ("B") REFERENCES "BlockNode"("id") ON DELETE CASCADE ON UPDATE CASCADE;
