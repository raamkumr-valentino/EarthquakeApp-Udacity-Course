# EarthquakeApp-Udacity-Course
  This Earthquake Android Application which list the recent earthquake data to the users based on user wish from USGS Earhquake Api. here the application contains Listview to list the single earthquake item.each item has earthquake magnitude,place and time and this application also Settings Preference to setting up our request URL based on user wish.
# Request URL
https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&limit=10&minmag=6

  format = geojson // json response,
  eventtype = earthquake //response event type,
  orderby = time //response order,
  limit = 10 //response limt,
  minmag = 6 //particular earthquake magnitude response.
# Android Settings 
  Apps often include settings that allow users to modify app features and behaviors.
  If you want to provide settings for your app, you should use Android's Preference APIs to build an interface that's consistent with the user experience in other Android apps (including the system settings). This document describes how to build your app settings using Preference APIs.
# Android Loaders
  Loader API lets you load data from a content provider or other data source for display in an Activity or Fragment based on Loader ID it avoid unwanted reload activity or fragment when configuration changes.
