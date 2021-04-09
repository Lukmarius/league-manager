import { IMatch } from 'app/entities/match/match.model';
import { ILeague } from 'app/entities/league/league.model';

export interface IRound {
  id?: number;
  roundNumber?: number | null;
  matches?: IMatch[] | null;
  league?: ILeague | null;
}

export class Round implements IRound {
  constructor(public id?: number, public roundNumber?: number | null, public matches?: IMatch[] | null, public league?: ILeague | null) {}
}

export function getRoundIdentifier(round: IRound): number | undefined {
  return round.id;
}
