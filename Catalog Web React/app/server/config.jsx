var currentEnv = process.env.NODE_ENV || 'development';
var port = process.env.PORT || 8000;

module.exports = {
  env: {
    development: currentEnv === 'development',
    production: currentEnv === 'production'
  },
  server: {
    port: port,
    url: "http://localhost:" + port + "/"
  }
};
