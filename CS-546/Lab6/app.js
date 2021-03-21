
const express = require('express');
const app = express();
const configRoutes = require('./routes');

app.use(express.json());

configRoutes(app);

app.listen(3000, () => {
  console.log("====================================================");
  console.log('CS-546 lab-6 server is running on http://localhost:3000');
});

