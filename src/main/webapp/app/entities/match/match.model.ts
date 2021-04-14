import { ITeam } from 'app/entities/team/team.model';
import { IMatchResult } from 'app/entities/match-result/match-result.model';
import { IRound } from 'app/entities/round/round.model';

export interface IMatch {
  id?: number;
  homeTeam?: ITeam | null;
  awayTeam?: ITeam | null;
  matchResult?: IMatchResult | undefined | null;
}

export class Match implements IMatch {
  constructor(
    public id?: number,
    public homeTeam?: ITeam | null,
    public awayTeam?: ITeam | null,
    public matchResult?: IMatchResult | undefined | null
  ) {}
}

export function getMatchIdentifier(match: IMatch): number | undefined {
  return match.id;
}
