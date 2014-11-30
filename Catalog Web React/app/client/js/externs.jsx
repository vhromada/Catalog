/*
 App specific externs.

 There are several ways how to prevent compiler from mangling symbols:
 @see http://stackoverflow.com/questions/11681070/exposing-dynamically-created-functions-on-objects-with-closure-compiler

 Put all app specific properties which should not be mangled here.
 It does not matter exact className, just property name.
 It will not be mangled across whole app.
 */
var game = function () {
};
game.prototype.id;
game.prototype.name;
game.prototype.wikiEn;
game.prototype.wikiCz;
game.prototype.mediaCount;
game.prototype.crack;
game.prototype.serialKey;
game.prototype.patch;
game.prototype.trainer;
game.prototype.trainerData;
game.prototype.editor;
game.prototype.saves;
game.prototype.otherData;
game.prototype.note;
game.prototype.position;

var expressjs = function () {
};
expressjs.prototype.json;
expressjs.prototype.use;
