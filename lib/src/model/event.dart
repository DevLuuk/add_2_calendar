import 'package:flutter/foundation.dart';

/// Class that holds each event's info.
class Event {
  int id;
  String title, description, location;
  DateTime startDate, endDate;
  bool allDay;

  Event(
      {this.id,
      @required this.title,
      this.description = '',
      this.location = '',
      @required this.startDate,
      @required this.endDate,
      this.allDay = false});
}
