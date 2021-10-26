import md5 from 'blueimp-md5';

const publickey = process.env.REACT_APP_PUBLIC_KEY;
const privatekey = process.env.REACT_APP_PRIVATE_KEY;

const ts = new Date().getTime();
const stringToHash = ts + privatekey + publickey;
const hash = md5(stringToHash);

const baseCharactersUrl = 'https://gateway.marvel.com:443/v1/public/characters';
const baseComicsUrl = 'https://gateway.marvel.com:443/v1/public/comics';
const baseSeriesUrl = 'https://gateway.marvel.com:443/v1/public/series';

const urlTail = '?ts=' + ts + '&apikey=' + publickey + '&hash=' + hash;

export const charactersUrl = baseCharactersUrl + '?ts=' + ts + '&apikey=' + publickey + '&hash=' + hash;
export const comicsUrl = baseComicsUrl + '?ts=' + ts + '&apikey=' + publickey + '&hash=' + hash;
export const seriesUrl = baseSeriesUrl + '?ts=' + ts + '&apikey=' + publickey + '&hash=' + hash;

export const characterDetailsUrl = (charId) => {
    return `${baseCharactersUrl}/${charId}${urlTail}`
}
export const comicDetailsUrl = (comicId) => {
    return `${baseComicsUrl}/${comicId}${urlTail}`
}
export const seriesDetailsUrl = (seriesId) => {
    return `${baseSeriesUrl}/${seriesId}${urlTail}`
}