/*
  Warnings:

  - You are about to drop the column `type` on the `Block` table. All the data in the column will be lost.

*/
-- AlterTable
ALTER TABLE "Block" DROP COLUMN "type";

-- DropEnum
DROP TYPE "BlockType";
