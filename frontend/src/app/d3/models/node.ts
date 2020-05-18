export class Node implements d3.SimulationNodeDatum {
  // optional - defining optional implementation properties - required for relevant typing assistance
  index?: number;
  x?: number;
  y?: number;
  vx?: number;
  vy?: number;
  fx?: number | null;
  fy?: number | null;

  id: string;
  text: string;

  constructor(id, text) {
    this.id = id;
    this.text = text;
  }

  get r() {
    return 15;
  }

  get fontSize() {
    return 14 + 'px';
  }

  get color() {
    return "rgb(0,106,197)";
  }
}
