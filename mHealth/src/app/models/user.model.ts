export class User {
  uuid: string | null = null;
  vorname: string | null = null;
  nachname: string | null = null;
  email: string | null = null;
  therapeut: boolean | null = null;
  username: string | null = null;
  techUser: boolean | null = null;
  schwellwert: number | null = null;
  toggle?: boolean | false = false;

  public isTherapeut(): boolean {
    if (this.therapeut == null) {
      return false;
    }
    return this.therapeut;
  }

  public isPatient(): boolean {
    return !(this.isTechUser() || this.isTherapeut());
  }

  public isTechUser(): boolean {
    if (this.techUser == null) {
      return false;
    }
    return this.techUser;
  }

  public get fullName(): string {
    return `${this.vorname} ${this.nachname}`;
  }

}
