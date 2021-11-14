export interface FragebogenModel {
  id: number;
  titel: string;
  beschreibung: string;
  author: string;
  json: string;
}

export interface FragebogenAbgeschlossenModel {
  id: number;
  titel: string;
  beschreibung: string;
  json: string;
  wert: number;
  erstellt: string;
}
