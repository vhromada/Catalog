goog.provide('app.games.Store');

goog.require('app.games.Game');
goog.require('goog.array');

/**
 * @param {este.Dispatcher} dispatcher
 * @param {app.Actions} actions
 * @param {app.Data} data
 * @constructor
 */
app.games.Store = function (dispatcher, actions, data) {
  this.actions = actions;
  this.data = data;

  dispatcher.register((function (_this) {
    return function (action, payload) {
      switch (action) {
        case app.Actions.GAME_DUPLICATE:
          return _this.duplicate(payload.id);
        case app.Actions.GAME_REMOVE:
          return _this.remove(payload.id);
        case app.Actions.GAME_MOVE_UP:
          return _this.moveUp(payload.id);
        case app.Actions.GAME_MOVE_DOWN:
          return _this.moveDown(payload.id);
        case app.Actions.GAME_UPDATE_POSITIONS:
          return _this.updatePositions();
      }
    };
  })(this));

  /**
   * @type {Array.<app.games.Game>}
   */
  this.games = [];

  /**
   * @type {number}
   */
  this.mediaCount = 0;
};

/**
 * @returns {!goog.Promise}
 */
app.games.Store.prototype.findAll = function () {
  return this.data.getGames().then(function (games) {
    this.games = games;
    this.mediaCount = 0;
    goog.array.forEach(this.games, function (element) {
      this.mediaCount += element.mediaCount;
    }.bind(this));
  }.bind(this)).then(function () {
    this.actions.renderApp();
  }.bind(this));
};

/**
 * @param {app.games.Game} game
 */
app.games.Store.prototype.add = function (game) {
  game.id = this.newId();
  goog.array.insert(this.games, game);
  this.mediaCount += game.mediaCount;
};

/**
 * @param {app.games.Game} game
 */
app.games.Store.prototype.edit = function (game) {
  var index = this.getIndex(game.id);
  this.mediaCount -= this.games[index].mediaCount;
  goog.array.removeAt(this.games, index);
  goog.array.insertAt(this.games, game, index);
  this.mediaCount += game.mediaCount;
};

/**
 * @param {number} id
 */
app.games.Store.prototype.duplicate = function (id) {
  var index = this.getIndex(id);
  var oldGame = this.games[index];
  var game = app.games.Game.duplicate(oldGame);
  game.id = this.newId();
  goog.array.insertAt(this.games, game, index + 1);
  this.mediaCount += game.mediaCount;
};

/**
 * @param {number} id
 */
app.games.Store.prototype.remove = function (id) {
  var game = this.games[this.getIndex(id)];
  goog.array.remove(this.games, game);
  this.mediaCount -= game.mediaCount;
};

/**
 * @param {number} id
 */
app.games.Store.prototype.moveUp = function (id) {
  var index = this.getIndex(id);
  this.switchPositions(index, index - 1);
};

/**
 * @param {number} id
 */
app.games.Store.prototype.moveDown = function (id) {
  var index = this.getIndex(id);
  this.switchPositions(index, index + 1);
};

app.games.Store.prototype.updatePositions = function () {
  goog.array.forEach(this.games, function (element, index) {
    element.position = index;
  });
};

/**
 * @returns {number}
 * @private
 */
app.games.Store.prototype.newId = function () {
  var id = 0;
  goog.array.forEach(this.games, function (element) {
    if (element.id > id) {
      id = element.id;
    }
  });
  return id + 1;
};

/**
 * @param {number} id
 * @returns {number}
 * @private
 */
app.games.Store.prototype.getIndex = function (id) {
  return goog.array.findIndex(this.games, function (_game) {
    return id == _game.id;
  });
};

/**
 * @param {number} index1
 * @param {number} index2
 * @private
 */
app.games.Store.prototype.switchPositions = function (index1, index2) {
  var game = this.games[index1];
  this.games[index1] = this.games[index2];
  this.games[index2] = game;
};
