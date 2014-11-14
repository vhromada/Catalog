goog.provide('app.games.Game');

goog.require('goog.string.StringBuffer');

/**
 @constructor
 */
app.games.Game = function () {

  /**
   @type {number}
   */
  this.id;

  /**
   @type {string}
   */
  this.name;

  /**
   @type {string}
   */
  this.wikiEn;

  /**
   @type {string}
   */
  this.wikiCz;

  /**
   @type {number}
   */
  this.mediaCount;

  /**
   @type {boolean}
   */
  this.crack;

  /**
   @type {boolean}
   */
  this.serialKey;

  /**
   @type {boolean}
   */
  this.patch;

  /**
   @type {boolean}
   */
  this.trainer;

  /**
   @type {boolean}
   */
  this.trainerData;

  /**
   @type {boolean}
   */
  this.editor;

  /**
   @type {boolean}
   */
  this.saves;

  /**
   @type {string}
   */
  this.otherData;

  /**
   @type {string}
   */
  this.note;

  /**
   @type {number}
   */
  this.position;
};

/**
 @param {Object} json
 @return {app.games.Game}
 */
app.games.Game.loadFromJson = function (json) {
  var game = new app.games.Game();
  game.id = json.id;
  game.name = json.name;
  game.wikiEn = json.wikiEn;
  game.wikiCz = json.wikiCz;
  game.mediaCount = json.mediaCount;
  game.crack = json.crack;
  game.serialKey = json.serialKey;
  game.patch = json.patch;
  game.trainer = json.trainer;
  game.trainerData = json.trainerData;
  game.editor = json.editor;
  game.saves = json.saves;
  game.otherData = json.otherData;
  game.note = json.note;
  game.position = json.position;

  return game;
};

/**
 @param {app.games.Game} game
 @return {app.games.Game}
 */
app.games.Game.duplicate = function (game) {
  var newGame = new app.games.Game();
  newGame.id = game.id;
  newGame.name = game.name;
  newGame.wikiEn = game.wikiEn;
  newGame.wikiCz = game.wikiCz;
  newGame.mediaCount = game.mediaCount;
  newGame.crack = game.crack;
  newGame.serialKey = game.serialKey;
  newGame.patch = game.patch;
  newGame.trainer = game.trainer;
  newGame.trainerData = game.trainerData;
  newGame.editor = game.editor;
  newGame.saves = game.saves;
  newGame.otherData = game.otherData;
  newGame.note = game.note;
  newGame.position = game.position;

  return newGame;
};

/**
 @returns {string}
 */
app.games.Game.prototype.getAdditionalData = function () {
  var result = new goog.string.StringBuffer();
  if (this.crack) {
    result.append('Crack');
  }
  this.addToResult(result, this.serialKey, 'serial key');
  this.addToResult(result, this.patch, 'patch');
  this.addToResult(result, this.trainer, 'trainer');
  this.addToResult(result, this.trainerData, 'data for trainer');
  this.addToResult(result, this.editor, 'editor');
  this.addToResult(result, this.saves, 'saves');
  if (!goog.string.isEmptyOrWhitespaceSafe(this.otherData)) {
    if (result.getLength() != 0) {
      result.append(', ');
    }
    result.append(this.otherData);
  }

  return result.toString();
};

/**
 @param {goog.string.StringBuffer} result
 @param {boolean} value
 @param {string} string
 @private
 */
app.games.Game.prototype.addToResult = function (result, value, string) {
  if (value) {
    if (result.getLength() == 0) {
      result.append(string.substring(0, 1).toUpperCase());
      result.append(string.substring(1));
    } else {
      result.append(', ');
      result.append(string);
    }
  }
};
