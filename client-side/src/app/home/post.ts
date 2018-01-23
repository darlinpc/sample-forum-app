import { User } from '../nav/user';
import { Category } from './category';
export interface Post {
  id: string;
  title: string;
  author: User;
  content: string;
  category: Category;
  createddate: string;
  comments: Array<string>;
}
