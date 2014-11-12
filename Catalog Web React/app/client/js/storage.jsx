goog.provide('app.Storage');

goog.require('este.Storage');

/**
 @param {app.Dispatcher} dispatcher
 @param {app.Routes} routes
 @constructor
 @extends {este.Storage}
 */
app.Storage = function (dispatcher, routes) {
  this.routes = routes;
  this.dispatcherId = dispatcher.register((function (_this) {
    return function (action, payload) {
      switch (action) {
        case app.Actions.LOAD_ROUTE:
          return _this.loadRoute_(payload.route, payload.params);
      }
    };
  })(this));
};

goog.inherits(app.Storage, este.Storage);

app.Storage.prototype.init = function () {
};

/**
 @param {este.Route} route
 @param {Object} params
 @return {!goog.Promise}
 @private
 */
app.Storage.prototype.loadRoute_ = function (route, params) {
  switch (route) {
    case this.routes.home:
    case this.routes.games:
      return this.ok();
    default:
      return this.notFound();
  }
};
