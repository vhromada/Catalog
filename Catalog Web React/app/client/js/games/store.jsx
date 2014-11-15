goog.provide('app.games.Store');

goog.require('app.games.Game');
goog.require('app.stores.Store');
goog.require('goog.array');
goog.require('goog.Promise');

/**
 * @param {app.stores.StoreRegistry} registry
 * @param {app.Dispatcher} dispatcher
 * @param {app.Actions} actions
 * @constructor
 * @extends {app.stores.Store}
 */
app.games.Store = function (registry, dispatcher, actions) {
  goog.base(this, registry);

  this.actions = actions;

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
      }
    };
  })(this));

  /**
   * @type {Array}
   */
  this.jsonGames = [
    app.games.Game.loadFromJson(
      {
        id: 1,
        name: 'Game 1',
        wikiEn: 'Wiki EN 1',
        wikiCz: 'Wiki CZ 1',
        mediaCount: 1,
        crack: true,
        serialKey: true,
        patch: true,
        trainer: true,
        trainerData: true,
        editor: true,
        saves: true,
        otherData: 'Other data 1',
        note: 'Note 1',
        position: 0
      }
    ),
    app.games.Game.loadFromJson(
      {
        id: 2,
        name: 'Game 2',
        wikiEn: 'Wiki EN 2',
        wikiCz: 'Wiki CZ 2',
        mediaCount: 2,
        crack: true,
        serialKey: false,
        patch: false,
        trainer: false,
        trainerData: false,
        editor: true,
        saves: false,
        otherData: 'Other data 2',
        note: 'Note 2',
        position: 1
      }
    ),
    app.games.Game.loadFromJson(
      {
        id: 3,
        name: 'Game 3',
        mediaCount: 1,
        crack: false,
        serialKey: false,
        patch: false,
        trainer: false,
        trainerData: false,
        editor: false,
        saves: false,
        position: 2
      }
    )
  ];

  /**
   * @type {Array.<app.games.Game>}
   */
  this.games = [];

  /**
   * @type {number}
   */
  this.mediaCount = 0;
};

goog.inherits(app.games.Store, app.stores.Store);

/**
 * @return {!goog.Promise}
 */
app.games.Store.prototype.findAll = function () {
  var resolver = function (resolve) {
    var games = this.jsonGames;
    var count = 0;
    goog.array.forEach(games, function (element) {
      count += element.mediaCount;
    });
    var result = [games, count];
    resolve(result);
  }.bind(this);

  var promise = new goog.Promise(resolver, this);

  return promise
    .then(function (result) {
      this.games = result[0];
      this.count = result[0].length;
      this.mediaCount = result[1];
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
  this.notify();
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
  this.notify();
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
