goog.provide('app.routes.Store');

goog.require('este.Store');
goog.require('goog.net.HttpStatus');

/**
 * @param {app.Actions} actions
 * @param {app.Dispatcher} dispatcher
 * @param {app.Routes} routes
 * @param {este.Router} router
 * @constructor
 * @extends {este.Store}
 */
app.routes.Store = function (actions, dispatcher, routes, router) {
  goog.base(this);

  this.actions = actions;
  this.dispatcher = dispatcher;
  this.routes = routes;
  this.router = router;
  this.dispatcher.register((function (_this) {
    return function (action, payload) {
      switch (action) {
        case app.Actions.LOAD_ROUTE:
          return _this.loadRoute_(payload);
      }
    };
  })(this));
};

goog.inherits(app.routes.Store, este.Store);

/**
 * @returns {*}
 */
app.routes.Store.prototype.start = function () {
  this.routes.addToEste(this.router, this.onRouteMatch_.bind(this));
  return this.router.start();
};

/**
 * @param {este.Route} route
 * @param {Object} params
 * @returns {!goog.Promise}
 * @private
 */
app.routes.Store.prototype.onRouteMatch_ = function (route, params) {
  return this.actions.loadRoute(route, params).then((function (_this) {
    return function () {
      return _this.routes.setActive(route, params);
    };
  })(this)).thenCatch((function (_this) {
    return function (reason) {
      return _this.routes.trySetErrorRoute(reason);
    };
  })(this)).then((function (_this) {
    return function () {
      return _this.actions.renderApp();
    };
  })(this));
};

/**
 * @param {Object}payload
 * @returns {goog.net.HttpStatus}
 //* @protected
 */
app.routes.Store.prototype.loadRoute_ = function (payload) {
  switch (payload.route) {
    case this.routes.home:
    case this.routes.games:
      return goog.net.HttpStatus.OK;
    default:
      return goog.net.HttpStatus.NOT_FOUND;
  }
};
