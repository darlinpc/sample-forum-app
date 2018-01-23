import {Post} from '../home/post';
import {User} from '../nav/user';
import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-post',
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.css']
})
export class ArticleComponent implements OnInit {
  model: any = {};
  post: Post;
  id;
  currentUser: User;
  sameUser;

  constructor(private http: HttpClient, private activatedRoute: ActivatedRoute, private router: Router) {
    this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    this.activatedRoute.url.subscribe(params => {
      this.id = params[1].path;
      console.log(this.id);
    });


  }

  ngOnInit() {

    this.loadData(this.id);
  }

  loadData(id) {
    this.http.get('http://localhost:8080/ForumApp/rest/posts/' + id).subscribe(data => {
      this.post = data;
      console.log(data);
      console.log(this.currentUser);

      if (this.currentUser.username === this.post.author.username) {
        this.sameUser = true;
        console.log(this.sameUser);
      }

    });
  }

  addComment() {
    let id = this.post.id;
    let comment = this.model.comment;
    let author = this.currentUser.id;

    let url = 'http://localhost:8080/ForumApp/rest/posts/addcomment?id=' + id
      + '&comment=' + comment + '&author=' + author;
    this.http.get(url).subscribe(data => {
      this.post = data;
      this.model.comment = "";
      console.log(data);
    });
  }

  edit() {
    let id = this.post.id;
    this.router.navigate(['/editor/' + id]);

  }

}
