

/**
 * Creates a new latitude/longitude pair with the specified values in degrees
 * @namespace neon.util
 * @class LatLon
 * @constructor
 * @param {double} latDegrees
 * @param {double} lonDegrees

 */
neon.util.LatLon = function (latDegrees, lonDegrees) {
    this.validateArgs_(latDegrees, lonDegrees);

    /**
     * The latitude in degrees
     * @property latDegrees
     * @type {double}
     */
    this.latDegrees = latDegrees;

    /**
     * The longitude in degrees
     * @property lonDegrees
     * @type {double}
     */
    this.lonDegrees = lonDegrees;

};

neon.util.LatLon.prototype.validateArgs_ = function (latDegrees, lonDegrees) {
    if (latDegrees > 90 || latDegrees < -90) {
        throw new Error('Invalid latitude ' + latDegrees + '. Must be in range [-90,90]');
    }

    if (lonDegrees > 180 || lonDegrees < -180) {
        throw new Error('Invalid longitude ' + lonDegrees + '. Must be in range [-180,180]');
    }
};