goog.provide('app.Actions');

/**
 @param {app.Dispatcher} dispatcher
 @constructor
 */
app.Actions = function (dispatcher) {
  this.dispatcher = dispatcher;
};

app.Actions.LOAD_ROUTE = 'load-route';
app.Actions.SYNC_VIEW = 'sync-view';

/**
 @param {este.Route} route
 @param {Object} params
 */
app.Actions.prototype.loadRoute = function (route, params) {
  return this.dispatcher.dispatch(app.Actions.LOAD_ROUTE, {
    route: route,
    params: params
  });
};

app.Actions.prototype.syncView = function () {
  return this.dispatcher.dispatch(app.Actions.SYNC_VIEW);
};
