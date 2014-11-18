goog.provide('app.Actions');

/**
 * @param {este.Dispatcher} dispatcher
 * @constructor
 */
app.Actions = function (dispatcher) {
  this.dispatcher = dispatcher;
};

app.Actions.LOAD_ROUTE = 'load-route';
app.Actions.RENDER_APP = 'render-app';

app.Actions.GAME_DUPLICATE = 'game-duplicate';
app.Actions.GAME_REMOVE = 'game-remove';
app.Actions.GAME_MOVE_UP = 'game-move-up';
app.Actions.GAME_MOVE_DOWN = 'game-move-down';
app.Actions.GAME_UPDATE_POSITIONS = 'game-update-positions';

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

/**
 * @param {number} id
 * @returns {goog.Promise}
 */
app.Actions.prototype.gameDuplicate = function (id) {
  return this.dispatcher.dispatch(app.Actions.GAME_DUPLICATE, {
    id: id
  }).then(function () {
    this.renderApp();
  }.bind(this));
};

/**
 * @param {number} id
 * @returns {goog.Promise}
 */
app.Actions.prototype.gameRemove = function (id) {
  return this.dispatcher.dispatch(app.Actions.GAME_REMOVE, {
    id: id
  }).then(function () {
    this.renderApp();
  }.bind(this));
};

/**
 * @param {number} id
 * @returns {goog.Promise}
 */
app.Actions.prototype.gameMoveUp = function (id) {
  return this.dispatcher.dispatch(app.Actions.GAME_MOVE_UP, {
    id: id
  }).then(function () {
    this.renderApp();
  }.bind(this));
};

/**
 * @param {number} id
 * @returns {goog.Promise}
 */
app.Actions.prototype.gameMoveDown = function (id) {
  return this.dispatcher.dispatch(app.Actions.GAME_MOVE_DOWN, {
    id: id
  }).then(function () {
    this.renderApp();
  }.bind(this));
};

/**
 * @returns {goog.Promise}
 */
app.Actions.prototype.gameUpdatePositions = function () {
  return this.dispatcher.dispatch(app.Actions.GAME_UPDATE_POSITIONS).then(function () {
    this.renderApp();
  }.bind(this));
};
