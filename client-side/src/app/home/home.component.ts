import {Component, OnInit} from '@angular/core';
import {HomeService} from './home.service';
import {Category} from './category';
import {Post} from './post';
import {HttpClient} from '@angular/common/http';
import { Router } from '@angular/router';


@Component({

  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  postTitle: 'Latest Posts';
  categories: Category[];
  posts: Post[];

  constructor(private _homeService: HomeService, private http: HttpClient, private router: Router) {}

  ngOnInit() {
    this.getCategories();
    this.getLatestPosts();
  }
  getCategories() {
    this.http.get('http://localhost:8080/ForumApp/rest/posts/topcategories').subscribe(data => {
      this.categories = data;
      console.log(data);
    });
  }

  getLatestPosts() {
    this.http.get('http://localhost:8080/ForumApp/rest/posts').subscribe(data => {
      this.posts = data;
      console.log(data);
      this.postTitle = 'Latest Posts';
    });
  }

  getPostByCategory(id) {
    this.http.get('http://localhost:8080/ForumApp/rest/posts/category/' + id).subscribe(data => {
      this.posts = data;
      this.postTitle = 'Showing posts in Category: ' + this.posts[0].category.name + ' (' + this.posts.length + ')';
      console.log(data);
    });
  }

  showPost(id) {
    this.router.navigate(['/post', id]);

  }
}

