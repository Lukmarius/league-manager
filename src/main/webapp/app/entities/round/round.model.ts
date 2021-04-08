import { IMatch } from 'app/entities/match/match.model';

export interface IRound {
  id?: number;
  roundNumber?: number | null;
  matches?: IMatch[] | null;
}

export class Round implements IRound {
  constructor(public id?: number, public roundNumber?: number | null, public matches?: IMatch[] | null) {}
}

export function getRoundIdentifier(round: IRound): number | undefined {
  return round.id;
}
