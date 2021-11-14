import {User} from './user.model';

export interface FragebogenZuweisung {
  id: number;
  cron: string;
  empfaenger: User;
}
