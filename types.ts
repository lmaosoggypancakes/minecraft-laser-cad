export enum BlockType {
  BLOCK,
  STAIRS,
  SLAB,
}

export enum Connection {
  ONE_TO_ONE, // #<>#
  STACK, //  #<>#<>#
  TRIANGLE, // #<>#
  //    #
  EVERYTHING, // a block on every side.
}

export interface Block {
  x: number;
  y: number;
  z: number;
  name: string; // rotation (1,2,3)
  id: number;
}

export interface BlockNode {
  val: Block;
  connections: number[];
}

export interface Box {
  blocks: Block[];
  id: number;
  name: string;
}
