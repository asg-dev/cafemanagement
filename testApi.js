var request = require('request');
var options = {
  'method': 'POST',
  'url': 'http://localhost:8080/api/carts?item_id=103&quantity=12',
  'headers': {
    'Authorization': 'Basic YXJhdmluZHNoaXZhOTZAZ21haWwuY29tOnRlc3Q=',
    'Content-Type': 'application/json',
  }
};
request(options, function (error, response) {
  if (error) throw new Error(error);
  console.log(response.body);
});

