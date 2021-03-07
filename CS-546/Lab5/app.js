const express = require('express');
const app = express();
const configRoutes = require('./routes');

configRoutes(app);

app.listen(3000, () => {
  console.log('CS-546 lab-5 server running on http://localhost:3000');
});
