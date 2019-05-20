import 'dart:async';

import 'package:add_2_calendar/src/model/event.dart';
import 'package:flutter/services.dart';

class Add2Calendar {
  static const MethodChannel _channel =
      const MethodChannel('flutter.javih.com/add_2_calendar');

  /// Add an Event (object) to user's default calendar.
  static Future<int> addEvent2Cal(Event event) async {
    final int isAdded =
        await _channel.invokeMethod('add2Cal', <String, dynamic>{
      'id': event.id,
      'title': event.title,
      'desc': event.description,
      'location': event.location,
      'startDate': event.startDate.millisecondsSinceEpoch,
      'endDate': event.endDate.millisecondsSinceEpoch,
      'allDay': event.allDay,
    });
    return isAdded;
  }

  /// Remove an Event (object) to user's default calendar.
  static Future<bool> removeEventFromCal(int eventId) async {
    final bool isRemoved =
        await _channel.invokeMethod('removeFromCal', <String, dynamic>{
      'id': eventId,
    });
    return isRemoved;
  }
}
