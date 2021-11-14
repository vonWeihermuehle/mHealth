export interface CustomDate {
  year: number;
  month: number;
  day: number;
}

export interface CustomTime {
  hour: number;
  minute: number;
  second: number;
}

export interface CustomDateModel {
  date: CustomDate;
  time: CustomTime;
}
