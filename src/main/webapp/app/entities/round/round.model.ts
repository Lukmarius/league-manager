export interface IRound {
  id?: number;
  roundNumber?: number | null;
}

export class Round implements IRound {
  constructor(public id?: number, public roundNumber?: number | null) {}
}

export function getRoundIdentifier(round: IRound): number | undefined {
  return round.id;
}
