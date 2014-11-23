goog.provide('server.Storage');

goog.require('goog.Promise');
goog.require('goog.net.HttpStatus');

/**
 * @param {app.Routes} routes
 * @constructor
 */
server.Storage = function (routes) {
  this.routes = routes;
};

/**
 * @returns {!goog.Promise}
 */
server.Storage.prototype.ok = function () {
  return goog.Promise.resolve(goog.net.HttpStatus.OK);
};

/**
 * @returns {!goog.Promise}
 */
server.Storage.prototype.notFound = function () {
  return goog.Promise.reject(goog.net.HttpStatus.NOT_FOUND);
};

/**
 * @param {este.Route} route
 * @param {Object} params
 * @returns {!goog.Promise}
 */
server.Storage.prototype.load = function (route, params) {
  switch (route) {
    case this.routes.home:
    case this.routes.games:
    case this.routes.addGame:
      return this.ok();
    default:
      return this.notFound();
  }
};
