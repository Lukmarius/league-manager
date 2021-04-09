import { IRound } from 'app/entities/round/round.model';
import { ILeagueStanding } from 'app/entities/league-standing/league-standing.model';

export interface ILeague {
  id?: number;
  name?: string;
  rounds?: IRound[] | null;
  leagueStandings?: ILeagueStanding[] | null;
}

export class League implements ILeague {
  constructor(
    public id?: number,
    public name?: string,
    public rounds?: IRound[] | null,
    public leagueStandings?: ILeagueStanding[] | null
  ) {}
}

export function getLeagueIdentifier(league: ILeague): number | undefined {
  return league.id;
}
