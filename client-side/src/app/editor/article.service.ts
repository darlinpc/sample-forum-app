import {Post} from '../home/post';
import { HttpClient } from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable()
export class ArticleService {

  constructor(private http: HttpClient) {}
  save(article): Observable<Post> {
    // If we're updating an existing article
    if (article.slug) {
      return this.http.put('/articles/' + article.slug, {article: article})
        .pipe(map(data => data.article));

      // Otherwise, create a new article
    } else {
      return this.http.post('/articles/', {article: article})
        .pipe(map(data => data.article));
    }
  }

}
