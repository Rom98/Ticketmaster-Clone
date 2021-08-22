
const express = require('express');
const path=require('path');
var hash = require('ngeohash');
const cors = require('cors');
const axios = require('axios');
var moment = require('moment'); // require

var SpotifyWebApi = require('spotify-web-api-node');

var spotifyApi = new SpotifyWebApi({
  clientId: '',
  clientSecret: '',
});

const app = express();
const distpath=path.join(__dirname,"/dist/collect-info");
app.use(express.static(distpath));




var geohash = "";
var convert_flag = false;
let return_data;

// app.use(cors());

app.use(function (req, res, next) {
  res.header("Access-Control-Allow-Origin", "*"); // update to match the domain you will make the request from
  res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
  next();
});

app.get('/',(req,res)=>{
  res.sendFile(path.join(__dirname,"/dist/collect-info/index.html"));
})

app.get('/form_data', (req, res) => {

  // res.header("Access-Control-Allow-Origin", "http://localhost:4200/");
  // //console.log(req.query.keyword);
  // //console.log(req.query.category);
  // //console.log(req.query.distance);
  // //console.log(req.query.metric);
  // //console.log(req.query.location);

  var keyword;
  var category;
  var distance;
  var metric;
  var location;

  keyword = req.query.keyword;
  category = req.query.category;
  distance = req.query.distance;
  metric = req.query.metric;
  location = req.query.location;

  if(distance == ""||distance==undefined||distance==null)
  {
    distance = 10;
  }

  var loc_x = 0;
  var loc_y = 0;

  categories = ["Music", "Sports", "Arts & Theatre", "Film", "Miscellaneous"]

  keyword = keyword.replace(" ", "+")

  if (category == categories[0])
    segmentId = "KZFzniwnSyZfZ7v7nJ"
  else if (category == categories[1])
    segmentId = "KZFzniwnSyZfZ7v7nE"
  else if (category == categories[2])
    segmentId = "KZFzniwnSyZfZ7v7na"
  else if (category == categories[3])
    segmentId = "KZFzniwnSyZfZ7v7nn"
  else
    segmentId = "KZFzniwnSyZfZ7v7n1"

  if (location.match(/[a-z]/i))
    convert_flag = true;

  if (category == "Default")
    ticket_link = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=&keyword=" + keyword + "&radius=" + distance + "&unit=" + metric + "&geoPoint="
  else
    ticket_link = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=&keyword=" + keyword + "&segmentId=" + segmentId + "&radius=" + distance + "&unit=" + metric + "&geoPoint="


 
  
  if (!convert_flag) {
    var loc = location.split(",");
    lat = parseFloat(loc[0]);
    lng = parseFloat(loc[1]);
    //geohash = hash.encode(Number(lat), Number(lng))
    get_coordinates(lat, lng)

  }
  else {                                                                 // If location string contains alphabets, the use googlemaps API
    var location = location.replace(/ /g, "+");    //(/\s+/g,"_")
    ////console.log("Typed location "+location);

    var loc_x = 0;
    var loc_y = 0;

    axios.get('https://maps.googleapis.com/maps/api/geocode/json?address=' + location + '&key=')
      .then((response) => {
        ////console.log(response.data)
        loc_x = response.data['results'][0]['geometry']['location'].lat;
        loc_y = response.data['results'][0]['geometry']['location'].lng;

        get_coordinates(loc_x, loc_y)
      })
      .catch(console.error)

  }
  async function get_coordinates(x, y) {
    ////console.log(loc_x, loc_y)
    geohash = hash.encode(x, y)
    ticket_link = ticket_link + geohash
    func();
    // //console.log(geohash);
    // var d = hash.decode(geohash)
    // //console.log(d)
  }

  //console.log(geohash)

  async function func() {
    console.log(ticket_link)

    axios.get(ticket_link)
      .then(async (response) => {
        let data = await extract_data(response.data);
        //console.log(data);
        res.send(data);

      })
      .catch(console.error)
  }




  async function extract_data(ticket_table_info) {

    ////console.log(ticket_table_info)
    //ticket_table_info = JSON.parse(obj)
    return_data = { "results": [] }
    genres_list = ""

  if(ticket_table_info.hasOwnProperty("_embedded"))                       // NOT WORKING!!!!!
    ticket_table_info = ticket_table_info["_embedded"]["events"]
  else
    return return_data

    names_str = []    //get names of all artists
    if (ticket_table_info != "Undefined") {
      for (let ticketInfo of ticket_table_info) {
        if (ticketInfo['_embedded'].hasOwnProperty("attractions")) {
          for (let nameInfo of ticketInfo['_embedded']['attractions'])
            if (!names_str.includes(nameInfo['name']))
              names_str.push(nameInfo['name']);
        }
      }
    }
    names_str = names_str.join(" | ")


    // if (i['_embedded']['attractions'] != "Undefined") {
    //   names_str = ticket_table_info[0]['_embedded']['attractions']['name'].join(' | ');
    // }
    //console.log(names_str);
    //console.log(names_str);
    //console.log(ticket_table_info[i]['_embedded']['attractions'][1]['name']);

    for (var i in ticket_table_info) {

      str = ""
      if ('subGenre' in ticket_table_info[i]['classifications'][0]) {
        if (ticket_table_info[i]['classifications'][0]['subGenre']['name'] != "Undefined")
          str = str + ticket_table_info[i]['classifications'][0]['subGenre']['name'] + " | "
      }

      if ('genre' in ticket_table_info[i]['classifications'][0]) {
        if (ticket_table_info[i]['classifications'][0]['genre']['name'] != "Undefined")
          str = str + ticket_table_info[i]['classifications'][0]['genre']['name'] + " | "
      }

      if ('segment' in ticket_table_info[i]['classifications'][0]) {
        if (ticket_table_info[i]['classifications'][0]['segment']['name'] != "Undefined")
          str = str + ticket_table_info[i]['classifications'][0]['segment']['name'] + " | "
      }

      if ('subType' in ticket_table_info[i]['classifications'][0]) {
        if (ticket_table_info[i]['classifications'][0]['subType']['name'] != "Undefined")
          str = str + ticket_table_info[i]['classifications'][0]['subType']['name'] + " | "
      }

      if ('type' in ticket_table_info[i]['classifications'][0]) {
        if (ticket_table_info[i]['classifications'][0]['type']['name'] != "Undefined")
          str = str + ticket_table_info[i]['classifications'][0]['type']['name']
      }

      str = str.substring(0, str.length - 3);
      console.log(ticket_table_info[i]['name'])


      return_data['results'].push({
        'Date': ticket_table_info[i]['dates']['start']['localDate'],
        'Event': ticket_table_info[i]['name'],
        'Id': ticket_table_info[i]['id'],
        'Artist': names_str.toString(),
        'Genre': str,
        'Venue': ticket_table_info[i]['_embedded']['venues'][0]['name']
      })

    }
    return_data['results'].sort(function (a, b) {                  //Sort in ascending order according to dates
      // Turn your strings into dates, and then subtract them
      // to get a value that is either negative, positive, or zero.
      return new Date(a.Date) - new Date(b.Date);
    });
    // //console.log(return_data)
    return return_data;
  }



});

app.get('/event_data', (req, res) => {
  id = req.query.Id;

  details_link = "https://app.ticketmaster.com/discovery/v2/events/" + id + ".json?apikey=";

  // console.log(details_link);

  axios.get(details_link)
    .then((response) => {
      ////console.log(response.data)
      return_data = { "results": [] }

      let i = response.data
      console.log(i)
      
      if(!i.hasOwnProperty("_embedded"))                       // NOT WORKING!!!!!
        return return_data

      names_str = []    //get names of all artists
      if (i != "Undefined") {
        if (i['_embedded'].hasOwnProperty("attractions")) {
          for (let nameInfo of i['_embedded']['attractions'])
            if (!names_str.includes(nameInfo['name']))
              names_str.push(nameInfo['name']);
        }

      }
      names_str = names_str.join(" | ")
      console.log("Name string: " + names_str)

      str = ""
      event__details_data = i;
      if ('subGenre' in event__details_data['classifications'][0]) {
        if (event__details_data['classifications'][0]['subGenre']['name'] != "Undefined")
          str = str + event__details_data['classifications'][0]['subGenre']['name'] + " | "
      }

      if ('genre' in event__details_data['classifications'][0]) {
        if (event__details_data['classifications'][0]['genre']['name'] != "Undefined")
          str = str + event__details_data['classifications'][0]['genre']['name'] + " | "
      }

      if ('segment' in event__details_data['classifications'][0]) {
        if (event__details_data['classifications'][0]['segment']['name'] != "Undefined")
          str = str + event__details_data['classifications'][0]['segment']['name'] + " | "
      }

      if ('subType' in event__details_data['classifications'][0]) {
        if (event__details_data['classifications'][0]['subType']['name'] != "Undefined")
          str = str + event__details_data['classifications'][0]['subType']['name'] + " | "
      }

      if ('type' in event__details_data['classifications'][0]) {
        if (event__details_data['classifications'][0]['type']['name'] != "Undefined")
          str = str + event__details_data['classifications'][0]['type']['name']
      }

      str = str.substring(0, str.length - 3);

      currency = i['priceRanges'][0]['currency']
      price_min = i['priceRanges'][0]['min']
      price_max = i['priceRanges'][0]['max']

      prices = String(Number(price_min)) + " - " + String(Number(price_max)) + " " + currency

      date = moment(i['dates']['start']['localDate']).format('ll');

      seat = "Undefined"
      if ('seatmap' in i)
        seat = i['seatmap']['staticUrl']

      return_data['results'].push({
        'Artist': names_str.toString(),
        'Event': i['name'],
        'Id' : i['id'],
        'Venue': i['_embedded']['venues'][0]['name'],
        'Time': date,
        'Category': str,
        'Price': prices,
        'Date':i['dates']['start']['localDate'],
        'TicketStatus': i['dates']['status']['code'],
        'BuyTicketAt': i['url'],
        'SeatMap': seat
      })

       console.log(return_data)


      res.send(return_data);
    })
    .catch(console.error)
});

app.get('/spotify_data', (req, res) => {

  var name = req.query.Event_Name;
  // artist_arr = []
  //artist_arr = name.split(" | ")

  //for(var i = 0 ; i < artist_arr.length ; i++)
  //{
  spotifyApi.searchArtists(name)                        //Calling searchArtist function for the first time
    .then(function (data) {
      // console.log('Search artists data"', data.body.artists.items);

      var spot_data = data.body;

      return_data = { "results": [] }

      for (var i = 0; i < spot_data.artists.items.length; i++) {
        if (spot_data.artists.items[i].name == name)           //Only if artist name matches given name i.e. the name passed here
        {
          return_data['results'].push({
            'Name': spot_data.artists.items[i].name,
            'Followers': spot_data.artists.items[i].followers.total,
            'Popularity': spot_data.artists.items[i].popularity,
            'Link': spot_data.artists.items[i].external_urls.spotify
          })
        }
      }
      //  console.log(return_data)

      res.send(return_data)
    },
      function (err) {
        // console.error(err);

        spotifyApi.clientCredentialsGrant().then(             // Retrieve an access token.
          function (data) {
            // //console.log('The access token expires in ' + data.body['expires_in']);
            // //console.log('The access token is ' + data.body['access_token']);

            spotifyApi.setAccessToken(data.body['access_token']);

            spotifyApi.searchArtists(name)                      //Call searchArtist function again after getting access token
              .then(function (data) {
                // console.log('Search artists data"', data.body.artists.items);

                var spot_data = data.body;

                return_data = { "results": [] }

                for (var i = 0; i < spot_data.artists.items.length; i++) {
                  if (spot_data.artists.items[i].name == name)           //Only if artist name matches given name i.e. the name passed here
                  {
                    return_data['results'].push({
                      'Name': spot_data.artists.items[i].name,
                      'Followers': spot_data.artists.items[i].followers.total,
                      'Popularity': spot_data.artists.items[i].popularity,
                      'Link': spot_data.artists.items[i].external_urls.spotify
                    })
                  }
                }


                //  console.log(return_data)

                res.send(return_data)

              },
                function (err) {
                  // console.error(err);
                }
              );

          },
          function (err) {
            ////console.log('Something went wrong when retrieving an access token', err);
          }
        );
      });
  //}
});

app.get('/venue_data', (req, res) => {
  var venue = req.query.Venue_Name;
  //venue = venue.replace(" ", "+")
  ////console.log(venue)

  venue_link = "https://app.ticketmaster.com/discovery/v2/venues.json?apikey=&keyword=" + venue
  ////console.log(venue_link)

  axios.get(venue_link)
    .then((response) => {
      ////console.log(response.data)
      
      
      //console.log(ven_data)
      return_data = { "results": [] }
      if(!response.data.hasOwnProperty("_embedded")){res.send(return_data)}

      var ven_data = response.data["_embedded"]["venues"][0]
      city = ven_data['city']['name'] + ", " + ven_data['state']['name']

      phone = ""
      openhrs = ""
      if (ven_data.hasOwnProperty("phoneNumberDetail"))
        phone = ven_data['boxOfficeInfo']['phoneNumberDetail']

      if (ven_data.hasOwnProperty('boxOfficeInfo') && ven_data['boxOfficeInfo'].hasOwnProperty("openHoursDetail"))
        openhrs = ven_data['boxOfficeInfo']['openHoursDetail']

      lat = parseFloat(ven_data['location']['latitude'])
      lon = parseFloat(ven_data['location']['longitude'])

      let general = "";

      let child = ""

      if (ven_data.hasOwnProperty("generalInfo") && ven_data['generalInfo'].hasOwnProperty("generalRule")) {
        general = ven_data['generalInfo']['generalRule'];
        child = ven_data['generalInfo']['childRule']
      };
      return_data['results'].push({
        'Address': ven_data['address']['line1'],
        'City': city,
        'Phone': phone,
        'Openhrs': openhrs,
        'latitude': lat,
        'longitude': lon,
        'General': general,
        'Child': child
      })

      //console.log(return_data)

      res.send(return_data)


    })
    .catch(console.error)

});

app.get('/suggestion_data', (req, res) => {
  var keyword = req.query.keyword;

  //console.log(keyword);
  suggest_link = "https://app.ticketmaster.com/discovery/v2/suggest?apikey=&keyword=" + keyword
  //console.log(suggest_link);

  axios.get(suggest_link)
    .then((response) => {

      res.send(response.data._embedded.attractions)
    })
});

const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
  //console.log(`App listening on port ${PORT}`);
  //console.log('Press Ctrl+C to quit.');
});
// [END gae_node_request_example]

module.exports = app;

