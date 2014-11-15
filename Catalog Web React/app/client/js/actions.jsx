goog.provide('app.Actions');

/**
 * @param {app.Dispatcher} dispatcher
 * @constructor
 */
app.Actions = function (dispatcher) {
  this.dispatcher = dispatcher;
};

app.Actions.LOAD_ROUTE = 'load-route';
app.Actions.RENDER_APP = 'render-app';

/**
 * @param {este.Route} route
 * @param {Object} params
 * @returns {goog.Promise}
 */
app.Actions.prototype.loadRoute = function (route, params) {
  return this.dispatcher.dispatch(app.Actions.LOAD_ROUTE, {
    route: route,
    params: params
  });
};

/**
 * @returns {goog.Promise}
 */
app.Actions.prototype.renderApp = function () {
  return this.dispatcher.dispatch(app.Actions.RENDER_APP);
};
