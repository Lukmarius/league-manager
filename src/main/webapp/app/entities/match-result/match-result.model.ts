export interface IMatchResult {
  id?: number;
  homeTeamScore?: number;
  awayTeamScore?: number;
}

export class MatchResult implements IMatchResult {
  constructor(public id?: number, public homeTeamScore?: number, public awayTeamScore?: number) {}
}

export function getMatchResultIdentifier(matchResult: IMatchResult): number | undefined {
  return matchResult.id;
}
