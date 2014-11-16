goog.provide('app.Data');

goog.require('goog.Promise');

/**
 * @constructor
 */
app.Data = function () {

  /**
   * @type {Array.<app.games.Game>}
   */
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
};

/**
 * @returns {goog.Promise}
 */
app.Data.prototype.getGames = function () {
  var resolver = function (resolve) {
    resolve(this.games);
  }.bind(this);

  return new goog.Promise(resolver, this);
};
