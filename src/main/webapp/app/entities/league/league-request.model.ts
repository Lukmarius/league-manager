import { IRound } from 'app/entities/round/round.model';
import { ILeagueStanding } from 'app/entities/league-standing/league-standing.model';
import { ITeam } from 'app/entities/team/team.model';

export interface ILeagueRequest {
  id?: number;
  name?: string;
  teams?: ITeam[] | null;
}

export class LeagueRequest implements ILeagueRequest {
  constructor(public id?: number, public name?: string, public teams?: ITeam[] | null) {}
}

export function getLeagueIdentifier(league: ILeagueRequest): number | undefined {
  return league.id;
}
