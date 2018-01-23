import {Post} from '../home/post';
import {User} from '../nav/user';
import {ArticleService} from './article.service';
import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, FormControl} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {FroalaEditorModule, FroalaViewModule} from 'angular-froala-wysiwyg';



@Component({

  templateUrl: './editor.component.html',
  styleUrls: ['./editor.component.css']
})
export class EditorComponent implements OnInit {
  model: any = {};
  id = null;
  saveButtonText;
  currentUser: User;
  article: Post = {} as Post;
  articleForm: FormGroup;
  tagField = new FormControl();
  errors: Object = {};
  isSubmitting = false;
  categories = [];

  constructor(
    private http: HttpClient,
    private articlesService: ArticleService,
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    // use the FormBuilder to create a form group
    this.articleForm = this.fb.group({
      title: '',
      description: '',
      body: '',
    });
    this.route.url.subscribe(params => {
      if (params[1]) {
        this.id = params[1].path;
        console.log(this.id);
      }

    });
  }

  ngOnInit() {
    this.loadCategories();
    if (this.id != null) {
      this.saveButtonText = 'Save';
      this.loadData();
    } else {
      this.saveButtonText = 'Publish Article';
    }


  }

  loadData() {
    const id = this.id;
    this.http.get('http://localhost:8080/ForumApp/rest/posts/' + id).subscribe(data => {
      this.model = data;
      this.model.category = data.category.name;
      console.log(data);
      console.log(this.currentUser);
    });
  }

  loadCategories() {
    this.http.get('http://localhost:8080/ForumApp/rest/posts/categories').subscribe(data => {
      this.categories = data;
      console.log(data);
    });
  }

  submitForm() {
    const title = this.model.title;
    const content = this.model.content;
    const category = this.model.category;
    const author = this.currentUser.id;

    let url;
    if (this.id) {
      url = 'http://localhost:8080/ForumApp/rest/posts/save';
    } else {
      url = 'http://localhost:8080/ForumApp/rest/posts/create';
    }


    this.http.post(url, {id: this.id, title: title, content: content, category: category, author: author}).subscribe(data => {
      this.router.navigate(['/']);

    });
  }

  updateArticle(values: Object) {
    Object.assign(this.article, values);
  }
}
