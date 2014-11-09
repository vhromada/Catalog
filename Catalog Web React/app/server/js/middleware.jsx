goog.provide('server.Middleware');

/**
 @param {Object} bodyParser
 @param {Function} compression
 @param {Function} methodOverride
 @constructor
 */
server.Middleware = function (bodyParser, compression, methodOverride) {
  this.bodyParser = bodyParser;
  this.compression = compression;
  this.methodOverride = methodOverride;
};

/**
 @param {Object} app Express app
 */
server.Middleware.prototype.use = function (app) {
  app.use(this.compression());
  app.use(this.bodyParser.json());
  return app.use(this.methodOverride());
};
