import { ITeam } from 'app/entities/team/team.model';
import { ILeague } from 'app/entities/league/league.model';

export interface ILeagueStanding {
  id?: number;
  position?: number;
  points?: number;
  scoredGoals?: number;
  lostGoals?: number;
  wins?: number;
  draws?: number;
  losses?: number;
  team?: ITeam;
  league?: ILeague | null;
}

export class LeagueStanding implements ILeagueStanding {
  constructor(
    public id?: number,
    public position?: number,
    public points?: number,
    public scoredGoals?: number,
    public lostGoals?: number,
    public wins?: number,
    public draws?: number,
    public losses?: number,
    public team?: ITeam,
    public league?: ILeague | null
  ) {}
}

export function getLeagueStandingIdentifier(leagueStanding: ILeagueStanding): number | undefined {
  return leagueStanding.id;
}
