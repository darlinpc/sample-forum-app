import {Injectable} from '@angular/core';
import {Category} from './category';
import {Post} from './post';
import {CATEGORY_ITEMS} from './category-data';
import {POST_ITEMS} from './post-data';
import {findIndex} from 'lodash';



@Injectable()
export class HomeService {
  private cItems = CATEGORY_ITEMS;
  private posts = POST_ITEMS;


  getCategories(): Category[] {
    console.log(this.cItems);
    return this.cItems;
  }
  getLatestPosts(): Post[] {

//    this.http.get('http://localhost:8080/ForumApp/rest/posts').subscribe(data => {
//      console.log(JSON.parse(data.toString()));
//    });
    console.log(this.posts);
    return this.posts;
  }
}

