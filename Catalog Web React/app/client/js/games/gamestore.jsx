goog.provide('app.games.GameStore');

goog.require('app.games.Game');
goog.require('app.stores.Store');
goog.require('goog.array');
goog.require('goog.Promise');

/**
 @param {app.stores.StoreRegistry} registry
 @constructor
 @extends {app.stores.Store}
 */
app.games.GameStore = function (registry) {
  goog.base(this, registry);

  this.games = [
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
    )
  ];

  /**
   @type {Array<app.games.Game>}
   */
  this.foundGames = [];
};

goog.inherits(app.games.GameStore, app.stores.Store);

/**
 @return {!goog.Promise}
 */
app.games.GameStore.prototype.findAll = function () {
  var resolver = function (resolve, reject) {
    resolve(this.games);
  };

  var promise = new goog.Promise(resolver, this);

  return promise
    .then(function (games) {
      this.foundGames = games;
      this.notify();
    }.bind(this));
};

/**
 @param {number} id
 @return {app.games.Game}
 */
app.games.GameStore.prototype.findById = function (id) {
  return goog.array.find(this.games, function (game) {
    return game.id == id;
  });
};

/**
 @param {app.games.Game} game
 */
app.games.GameStore.prototype.add = function (game) {
  var lastGame = goog.array.peek(this.games);
  game.id = lastGame.id + 1;
  goog.array.insert(this.games, game);
  this.notify();
};

/**
 @param {app.games.Game} game
 */
app.games.GameStore.prototype.edit = function (game) {
  var index = goog.array.findIndex(this.games, function (_game, index, games) {
    return game.id == _game.id;
  });
  goog.array.removeAt(this.games, index);
  goog.array.insertAt(this.games, game, index);
  this.notify();
};

/**
 @param {app.games.Game} game
 */
app.games.GameStore.prototype.remove = function (game) {
  goog.array.remove(this.games, game);
  this.notify();
};
