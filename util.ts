import type { Block, BlockNode, BlockType } from "@/types";
import { writeFileSync } from "fs";
import {
  DxfWriter,
  TextHorizontalAlignment,
  TextVerticalAlignment,
  Units,
  point3d,
} from "@tarikjabiri/dxf";

export const isValidBlock = (block: Block) => {
  if (block.x < 0 || block.y <= 0 || block.y < 0) return false;
};

export const generateRandomBlocks = (n: number): Block[] => {
  console.log("generating");
  let blocks: Block[] = [];
  for (let i = 0; i < n; i++) {
    const x = Math.round(Math.random() * 10);
    const y = Math.round(Math.random() * 10);
    const z = Math.round(Math.random() * 10);

    const orientation = Math.round(Math.random() * 4 + 1);

    const t = BlockType.BLOCK;

    blocks.push({ x, y, z, orientation, type: t } as Block);
  }
  console.log("generated");
  //   exportListToJson(blocks, "data.json");
  return blocks;
};

export const generateRandomConnections = (blocks: Block[]): BlockNode[] => {
  const nodes: BlockNode[] = [];
  blocks.forEach((block) => {
    nodes.push();
  });
  return nodes;
};
export const createNManyBlocks = (n: number, page: number) => {
  if (n < 0) throw new Error("n must be >= 0");
  const dxf = new DxfWriter();
  const S = 7.74805;
  const L = 10.255;

  const face = dxf.addBlock("face");

  face.addLine(point3d(10, 10), point3d(10, 10 - S));
  face.addLine(point3d(10, 0), point3d(10 + L, 0));
  face.addLine(point3d(10 + L, 0), point3d(10 + L, S));
  face.addLine(point3d(10 + L, S), point3d(10 + 2 * L, S));
  face.addLine(point3d(10 + 2 * L, S), point3d(10 + 2 * L, 0));
  face.addLine(point3d(10 + 2 * L, 0), point3d(10 + 3 * L, 0)),
    face.addLine(point3d(10 + 3 * L, 0), point3d(10 + 3 * L, S));
  face.addLine(point3d(10 + 3 * L, S), point3d(10 + 5 * L, S));

  face.addLine(point3d(10 + 5 * L, S), point3d(10 + 5 * L, S + L));
  face.addLine(point3d(10 + 5 * L, S + L), point3d(10 + 5 * L - S, S + L));
  face.addLine(
    point3d(10 + 5 * L - S, S + L),
    point3d(10 + 5 * L - S, S + L * 2)
  );
  face.addLine(
    point3d(10 + 5 * L - S, S + L * 2),
    point3d(10 + 5 * L, S + L * 2)
  );
  face.addLine(point3d(10 + 5 * L, S + L * 2), point3d(10 + 5 * L, S + L * 3));
  face.addLine(
    point3d(10 + 5 * L, S + L * 3),
    point3d(10 + 5 * L - S, S + L * 3)
  );
  face.addLine(
    point3d(10 + 5 * L - S, S + L * 3),
    point3d(10 + 5 * L - S, S + L * 5)
  );

  face.addLine(
    point3d(10 + 5 * L - S, S + L * 5),
    point3d(10 + 4 * L - S, S + L * 5)
  );
  face.addLine(
    point3d(10 + 4 * L - S, S + L * 5),
    point3d(10 + 4 * L - S, L * 5)
  );
  face.addLine(point3d(10 + 4 * L - S, L * 5), point3d(10 + 3 * L - S, L * 5));
  face.addLine(
    point3d(10 + 3 * L - S, L * 5),
    point3d(10 + 3 * L - S, L * 5 + S)
  );
  face.addLine(
    point3d(10 + 3 * L - S, L * 5 + S),
    point3d(10 + 2 * L - S, L * 5 + S)
  );
  face.addLine(
    point3d(10 + 2 * L - S, L * 5 + S),
    point3d(10 + 2 * L - S, L * 5)
  );
  face.addLine(point3d(10 + 2 * L - S, L * 5), point3d(10 - S, L * 5));

  face.addLine(point3d(10 - S, L * 5), point3d(10 - S, L * 4));
  face.addLine(point3d(10 - S, L * 4), point3d(10, L * 4));
  face.addLine(point3d(10, L * 4), point3d(10, L * 3));
  face.addLine(point3d(10, L * 3), point3d(10 - S, L * 3));
  face.addLine(point3d(10 - S, L * 3), point3d(10 - S, L * 2));
  face.addLine(point3d(10 - S, L * 2), point3d(10, L * 2));
  face.addLine(point3d(10, L * 2), point3d(10, 0));
  for (let i = 0; i < n; i++) {
    for (let j = 0; j < 6; j++) {
      dxf.addInsert(face.name, point3d(10 + j * 80, 10 + i * 80));
      const textPoint = point3d(5 + j * 80 + 20, 10 + i * 80 + 30);
      dxf.addText(
        textPoint,
        1,
        `${i + 1 + (page - 1) * 10} : ${j + 1 + (page - 1) * 10}`,
        {
          horizontalAlignment: TextHorizontalAlignment.Center,
          verticalAlignment: TextVerticalAlignment.Middle,
          rotation: 0,
          secondAlignmentPoint: textPoint,
        }
      );
    }
  }
  dxf.addText(point3d(550, 750), 1, `PAGE ${page}`, {
    horizontalAlignment: TextHorizontalAlignment.Center,
    verticalAlignment: TextVerticalAlignment.Middle,
    rotation: 0,
    secondAlignmentPoint: point3d(550, 750),
  });
  return dxf.stringify();
};

export const splitArray = (array: Array<any>, size: number) => {
  let result = [];
  for (let i = 0; i < array.length; i += size) {
    let chunk = array.slice(i, i + size);
    result.push(chunk);
  }
  return result;
};
